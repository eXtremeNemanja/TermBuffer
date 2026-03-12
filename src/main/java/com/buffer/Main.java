package com.buffer;

import com.buffer.textBuffer.Position;
import com.buffer.textBuffer.TextBuffer;

public class Main {

    static void main() {
        demonstrate();
    }

    private static void demonstrate() {
        // Create a text buffer - width: 10, height: 5, max scrollback: 10
        TextBuffer buffer = new TextBuffer(5, 3, 10);

        // Write some text
        buffer.writeText("Hello, World!");

        // Show the screen content
        System.out.println("=== Screen ===");
        System.out.println(buffer.getScreenContent());

        // Insert empty line (simulates scrolling)
        buffer.insertEmptyLine();

        // Show new screen state
        System.out.println("=== Screen after inserting empty line ===");
        System.out.println(buffer.getScreenContent());

        // Move cursor and insert text
        buffer.setCursorPosition(new Position(2, 1));
        buffer.writeText("ABC");

        System.out.println("=== Screen after writing 'ABC' at (2,1) ===");
        System.out.println(buffer.getScreenContent());

        // Show full buffer content (screen + scrollback)
        System.out.println("=== Full buffer (including scrollback) ===");
        System.out.println(buffer.getEntireContent());
    }
}
