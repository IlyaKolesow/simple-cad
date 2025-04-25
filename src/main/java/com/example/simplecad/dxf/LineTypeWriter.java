package com.example.simplecad.dxf;

import com.example.simplecad.modes.LineType;

import java.io.BufferedWriter;
import java.io.IOException;

public class LineTypeWriter {
    private BufferedWriter writer;

    public void write(BufferedWriter writer) throws IOException {
        this.writer = writer;
        writeContinuous();
        writeDashed();
        writeDashDot();
        writeDashDotDot();
    }

    private void writeContinuous() throws IOException {
        writer.write("  0\r\n");
        writer.write("LTYPE\r\n");
        writer.write("  2\r\n");
        writer.write("CONTINUOUS\r\n");
        writer.write("  73\r\n");
        writer.write("0\r\n");
    }

    private void writeDashed() throws IOException {
        writer.write("  0\r\n");
        writer.write("LTYPE\r\n");
        writer.write("  2\r\n");
        writer.write("DASHED\r\n");
        writePattern(LineType.DASH_DOT_DOT, 2);
    }

    private void writeDashDot() throws IOException {
        writer.write("  0\r\n");
        writer.write("LTYPE\r\n");
        writer.write("  2\r\n");
        writer.write("DASHDOT\r\n");
        writePattern(LineType.DASH_DOT, 4);
    }

    private void writeDashDotDot() throws IOException {
        writer.write("  0\r\n");
        writer.write("LTYPE\r\n");
        writer.write("  2\r\n");
        writer.write("DIVIDE\r\n");
        writePattern(LineType.DASH_DOT_DOT, 6);
    }

    private double getPatternLength(LineType lineType) {
        return lineType.getPattern().stream()
                .reduce(Double::sum)
                .get();
    }

    private void writePattern(LineType lineType, int partsCount) throws IOException {
        writer.write("  40\r\n");
        writer.write(getPatternLength(lineType) + "\r\n");
        writer.write("  73\r\n");
        writer.write(partsCount + "\r\n");
        for (int i = 0; i < partsCount; i++) {
            writer.write("  49\r\n");
            writer.write(lineType.getPattern().get(i) + "\r\n");
        }
    }
}
