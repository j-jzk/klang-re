package cz.j_jzk.klang.lex.re

import cz.j_jzk.klang.lex.re.fa.NFA
import cz.j_jzk.klang.lex.re.fa.StateFactory
import cz.j_jzk.klang.lex.re.parsing.parse
import cz.j_jzk.klang.lex.re.parsing.tokenize

// This is thread-safe, so it's okay to use a static instance. Although,
// it does make unit testing impossible.
private val faConstructor = RegexFAConstructor(StateFactory())

fun compileRegex(string: String): CompiledRegex {
    val tokens = tokenize(string)
    val expression = parse(tokens)
    return CompiledRegex(faConstructor.constructFrom(expression), string)
}

class CompiledRegex (val fa: NFA, val originalRegex: String) {
    fun matches(string: String): Boolean {
        return fa.matches(string)
    }

    override fun equals(other: Any?): Boolean =
        if (other is CompiledRegex)
            other.originalRegex == this.originalRegex
        else
            false

    override fun toString() = "CompiledRegex(/$originalRegex/)"
    override fun hashCode() = originalRegex.hashCode()
}
