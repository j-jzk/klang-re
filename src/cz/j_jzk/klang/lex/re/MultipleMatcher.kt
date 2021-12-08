package cz.j_jzk.klang.lex.re

import cz.j_jzk.klang.lex.re.fa.NFA
import cz.j_jzk.klang.lex.re.fa.State

/* TODO:
 *  - identify the regexes by an ID - ???
 *    - will have to think through whether this would actually save time, because
 *      everything is passed by reference anyways and map access is O(1), so looking
 *      up the regexes by an ID could just decrease performance
 *  - better separation of concerns
 *  - return just the longest match to save memory - ???
 *    - we would have to explicitly account for the cases where multiple matches
 *      are the longest, which may cost more processing time than what we would save
 *  - make MultipleMatcher consume some sort of stream insted of strings
 *  - javadoc
 */

class MultipleMatcher(val regexes: List<NFA>) {
	fun matchStart(input: String): Map<NFA, Int> {
		val matchesInProgress = mutableMapOf<NFA, MutableSet<State>>()
		val matched = mutableMapOf<NFA, Int>()

		// initialize
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

				// if the regex couldn't match the character, mark it for removal
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
}