# klang-re
## Quick reference
klang-re currently supports the following regular expression syntax:

**Expression** |  **Meaning**
--- | ---
`a\|b` | `a` or `b`
`(a)` | `a`
`a*` | `a` 0 or more times
`a+` | `a` 1 or more times
`a?` | `a` 0 or 1 time
`.` | any character
`[xy0-9]` | `x`, `y`, or any digit
`[^xy0-9]` | anything except the listed characters
`\w`, `\d`, ... |  any character from the predefined character class

### Character classes
**Character class** | **Meaning**
--- | ---
`\s` | whitespace
`\S` | not whitespace
`\d` | a digit
`\D` | not a digit
`\w` | a "word character" (`[a-zA-Z0-9_]`)
`\W` | not a word character
`\x` | a hexadecimal digit
`\O` | an octal digit

## About
A library used internally in [klang](https://github.com/j-jzk/klang), the toolkit for defining lexers and parsers for programming languages.

It is based on [Kotlex](https://github.com/Kevinpgalligan/Kotlex/) by Kevin Galligan.

## Technical Information
### How to Build
The project is built using the Maven build system. Clone the repository, then run `./mvnw package` from the top-level directory, or `./mvnw install` to install this package into the Maven local repository.

### Formal grammar
```
REGEXP                  -> OR | epsilon

OR                      -> CONCATENATION OR_OPTIONAL
CONCATENATION           -> EXPRESSION | EXPRESSION CONCATENATION
OR_OPTIONAL             -> "|" CONCATENATION OR_OPTIONAL | epsilon

UNIT                    -> UNMODIFIED_UNIT MODIFIER
UNMODIFIED_UNIT         -> GROUP | CHAR_MATCHER
MODIFIER                -> "*" | "+" | "?" | epsilon
GROUP                   -> "(" REGEXP ")"

CHAR_MATCHER            -> "." | NON_SPECIAL_CHARACTER | CHARACTER_CLASS | CHARACTER_RANGE
NON_SPECIAL_CHARACTER   -> "a" | "b" | ... | "\" SPECIAL_CHARACTER
SPECIAL_CHARACTER       -> "." | "(" | ")" | "." | "\" | "|"

CHARACTER_CLASS         -> "\" CHARACTER_CLASS_NAME
CHARACTER_CLASS_NAME    -> "s" | "d" | "w" | "S" | "D" | "W" | ...

CHARACTER_RANGE         -> "[" "^"? CHARACTER_RANGE_DEFINITION+ "]"
CHARACTER_RANGE_DEFINITION -> RANGE_NON_SPECIAL_CHARACTER
						    | RANGE_NON_SPECIAL_CHARACTER "-" RANGE_NON_SPECIAL_CHARACTER
RANGE_NON_SPECIAL_CHARACTER -> "a" | "b" | ... | "\" RANGE_SPECIAL_CHARACTER
RANGE_SPECIAL_CHARACTER -> "^" | "]" | "-" | "\"
```

## License
MIT.
