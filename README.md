# Terminal Text Buffer Implementation

## Overview

This project implements a simplified **terminal text buffer**, similar to the internal buffer used by terminal emulators.

The buffer stores characters in a grid of cells representing the **visible terminal screen** and maintains a **scrollback history** for lines that scroll off the screen.


# Data Model

The terminal buffer is composed of two logical parts:

## Screen

The **screen** represents the currently visible terminal area.

It is implemented as a fixed-size grid:

`
List<List<Cell>>
`

Each row represents a terminal line. The screen dimensions are defined by:

- width
- height

## Scrollback

Lines that move above the screen are stored in a **scrollback buffer**.

`
List<List<Cell>>
`

Scrollback preserves terminal history and allows previously visible lines to be accessed even after scrolling.

The maximum scrollback size is configurable. When the limit is reached, the oldest lines are discarded.

# Cell Representation

Each terminal cell contains:

- Character
- Cell Attributes
    - Foreground color
    - Background color
    - Style flags

Attributes are stored together with the character to simplify rendering and editing operations.

# Cursor

The buffer maintains a **cursor** that represents the position where the next character will be written.

The cursor contains:

- Row
- Column

Cursor always stays inside the **screen bounds**, ensuring it never moves outside the visible grid.

# Editing Operations

## Cursor-based operations

These operations depend on the current cursor position.

### Write text

Writes characters starting at the cursor position, **overwriting existing content** and moving the cursor forward.

If the cursor reaches the end of a line, the text **wraps to the next line**.

If the cursor moves beyond the bottom of the screen, the buffer **scrolls** and the top line moves into scrollback.

### Insert text

Inserts characters at the cursor position while **shifting existing characters to the right**.

If the inserted text exceeds the remaining space on the line, characters at the end of the line are **wrapped to next line**.

The cursor moves to the end of the inserted text.

### Fill line

Fills a screen line with a given character (or spaces).

This operation **does not modify the cursor position**.

## Non-cursor operations

These operations modify the buffer independently of the cursor position.

### Insert empty line

Adds an empty line to the **bottom of the screen**.

The top screen line is moved into the **scrollback buffer**, even if it contains only spaces.

### Clear screen

Replaces the screen contents with empty lines and resets the cursor to `(0,0)`.

The scrollback history is preserved.

### Clear screen and scrollback

Clears both the screen and the scrollback history and resets the cursor to `(0,0)`.

# Design Decisions

## Grid representation

The screen is implemented as a **mutable grid** using `List<List<Cell>>`.

This allows:

- direct random access to cells
- efficient updates

## Scrollback management

Scrollback is implemented using a dynamic list with a **fixed maximum size**.

When the limit is reached, the **oldest lines are discarded**.

## Attribute handling

Cell attributes are stored directly inside each cell to keep the model simple and avoid shared state.

This approach prioritizes **cell independence** over memory optimization.

# Testing

The project includes **comprehensive unit tests** that verify:

- cursor movement boundaries
- text writing and wrapping
- insertion behavior
- scrolling and scrollback handling
- screen clearing operations
- content access methods

Tests also cover **edge cases and boundary conditions**, such as:

- buffer initialization with negative dimensions
- writing and inserting text across multiple lines
- writing and inserting empty text
- cursor movement outside screen bounds

# Possible Improvements

Several extensions could further improve the buffer implementation:

- Support for wide characters (CJK characters and emoji)
- ANSI escape sequence parsing
- Dynamic screen resizing
- More efficient attribute storage
- Optimized memory usage for large scrollback histories

---

# Build and Run

Build the project:

```bash
mvn clean package
```

Run the tests:

```bash
mvn test
```