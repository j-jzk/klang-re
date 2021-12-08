package cz.j_jzk.klang.lex.re

import cz.j_jzk.klang.lex.re.fa.NFA
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MultipleMatcherTest {
	private val aPlus = compileRegex("a+").fa
	private val xPlus = compileRegex("x+").fa
	private val plus = compileRegex("\\+").fa
	private val plusPlus = compileRegex("\\++").fa

	@Test
	fun testWholeStringMatch() {
		assertEquals(
			mapOf(aPlus to 4),
			matchStart("aaaa", listOf(aPlus, xPlus))
		)
	}

	@Test
	fun testIncompleteMatch() {
		assertEquals(
			mapOf(aPlus to 4),
			matchStart("aaaax", listOf(aPlus, xPlus))
		)
	}

	@Test
	fun testMultipleMatches() {
		assertEquals(
			mapOf(
				plus to 1,
				plusPlus to 2
			),
			matchStart("++-", listOf(plus, plusPlus))
		)
	}

	@Test
	fun testNoMatch() {
		assertEquals(
			mapOf(),
			matchStart("fghkjfklj", listOf(aPlus, xPlus))
		)
	}

	@Test
	fun testRewindsInputCorrectly() {
		val iterator = "asdf".toList().listIterator()
		val matcher = MultipleMatcher(
			listOf(
				compileRegex("a").fa,
				compileRegex("s").fa
			),
			iterator
		)

		matcher.nextMatch() // should match the 'a'
		assertEquals('s', iterator.next())
		iterator.previous()

		matcher.nextMatch() // should match the 's'
		assertEquals('d', iterator.next())
	}

	private fun matchStart(string: String, regexes: List<NFA>) = MultipleMatcher(regexes, string.toList().listIterator()).nextMatch()
}
