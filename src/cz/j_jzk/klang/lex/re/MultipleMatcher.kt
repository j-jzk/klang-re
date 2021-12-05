package cz.j_jzk.klang.lex.re

import cz.j_jzk.klang.lex.re.fa.NFA
import cz.j_jzk.klang.lex.re.fa.State

/* TODO:
 *  - put this into a class
 *  - identify the regexes by an ID
 *  - unspaghettify
 *  - better separation of concerns
 */

fun matchStart(input: String, regexes: List<NFA>): Map<NFA, Int> {
	val matchesInProgress = mutableMapOf<NFA, MutableSet<State>>()
	val matched = mutableMapOf<NFA, Int>()

	for (re in regexes) {
		matchesInProgress[re] = mutableSetOf(re.startState)
	}

	for ((i, c) in input.withIndex()) {
		val toRemove = mutableListOf<NFA>()

		for ((re, states) in matchesInProgress) {
			val nextStates = re.getStates(states, c)
			matchesInProgress[re] = nextStates

			re.expandWithEpsilonTransitions(nextStates)
			if (nextStates.any { it.isAccepting })
				matched[re] = i + 1

			if (nextStates.isEmpty())
				toRemove += re
		}

		for (re in toRemove)
			matchesInProgress.remove(re)

		if (matchesInProgress.isEmpty())
			break
	}

	return matched
}
