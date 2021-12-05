package cz.j_jzk.klang.lex.re

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
}
