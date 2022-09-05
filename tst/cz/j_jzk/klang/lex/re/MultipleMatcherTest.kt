package cz.j_jzk.klang.lex.re

import cz.j_jzk.klang.lex.re.fa.NFA
import cz.j_jzk.klang.lex.re.nextMatchFromMultiple
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MultipleMatcherTest {
	private val aPlus = compileRegex("a+").fa
	private val xPlus = compileRegex("x+").fa
	private val aPlusX = compileRegex("a+x").fa
	private val dot = compileRegex(".").fa

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
				dot to 1,
				aPlus to 2
			),
			matchStart("aaz", listOf(dot, aPlus))
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
	// test if the matcher is not too greedy - if it doesn't return false matches
	fun testNotTooGreedy() {
		assertEquals(
			mapOf(dot to 1),
			matchStart("aaaaz", listOf(aPlusX, dot))
		)

		// to be safe
		assertEquals(
			matchStart("aaaaz", listOf(aPlusX, dot)),
			matchStart("aaaaz", listOf(dot, aPlusX))
		)
	}

	@Test
	fun testRewindsInputCorrectly() {
		val iterator = "asdf".toList().listIterator()
		val regexes = listOf(
			compileRegex("a").fa,
			compileRegex("s").fa
		)

		nextMatchFromMultiple(regexes, iterator) // should match the 'a'
		assertEquals('s', iterator.next())
		iterator.previous()

		nextMatchFromMultiple(regexes, iterator) // should match the 's'
		assertEquals('d', iterator.next())
	}

	@Test
	fun testRewindsInputCorrectlyOnNoMatch() {
		val iterator = "aa".toList().listIterator()
		val regexes = listOf(xPlus, aPlusX)

		nextMatchFromMultiple(regexes, iterator)
		assertEquals('a', iterator.next())
		assertEquals('a', iterator.next())
	}

	private fun matchStart(string: String, regexes: List<NFA>) = nextMatchFromMultiple(regexes, string.toList().listIterator())
}
