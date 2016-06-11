/*
 * Copyright (C) 2016 hiendv
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author hiendv
 */
public class Merger implements app.contracts.Merger {

    protected String input;
    protected String output;
    protected int chunkCount;
    protected int[] heap;
    int lastPick = -1;
    int finishedCount = 0;
    long[] skips;

    public Merger(String input, String output, int chunkCount) {
        this.input = input;
        this.output = output;
        this.chunkCount = chunkCount;
        this.heap = new int[chunkCount];
        this.setUpIO();
    }

    private void setUpIO() {
        File outputDirectory = new File(this.output).getParentFile();
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }
    }

    protected String findMin() {
        if (lastPick == -1 || skips[lastPick] == -1) {
            // lastPick is not set or belongs to a "dead" chunk
            // Get a new one
            for (int i = 0; i < this.chunkCount; i++) {
                if (skips[i] == -1) {
                    continue;
                }
                if (skips[i] >= 0) {
                    lastPick = i;
                }
            }
        }

        int min = heap[lastPick];

        for (int i = 0; i < this.chunkCount; i++) {
            if (skips[i] == -1) {
                // "Dead" chunk
                continue;
            }
            if (lastPick == i) {
                // Itself
                continue;
            }
            if (heap[i] < min) {
                min = heap[i];
                lastPick = i;
            }
        }

        return min + "\n";
    }

    @Override
    public void merge() throws IOException {
        BufferedWriter outputWriter;
        outputWriter = new BufferedWriter(
            new FileWriter(this.output)
        );

        skips = new long[heap.length];

        while (finishedCount < this.chunkCount) {
            for (int i = 0; i < this.chunkCount; i++) {
                // If the chunk runs out of elements, skip it
                if (skips[i] == -1) {
                    // "Dead" chunk
                    continue;
                }

                // If the last pick is not the index
                // and not the initial value
                // and the related chunk is not dead
                if (lastPick != i && lastPick != -1 && skips[lastPick] != -1) {
                    continue;
                }

                FileInputStream inputStream = new FileInputStream(this.input + "/" + i);

                inputStream.skip(skips[i]);
                Scanner scanner = new Scanner(inputStream, "UTF-8");

                if (!scanner.hasNextLine()) {
                    // If the chunk doesnt have any line left
                    finishedCount++;
                    skips[i] = -1;
                    continue;
                }

                // Update the heap at the index `i`
                heap[i] = Integer.parseInt(scanner.nextLine());

                if (lastPick == -1 && i < this.chunkCount - 1) {
                    // The heap is not completely loaded
                    continue;
                }

                // Find the minimum value
                String minStr = this.findMin();

                // Write it out and update the skips[lastPick]
                outputWriter.write(minStr);
                skips[lastPick] += minStr.length();
                outputWriter.flush();
            }
        }
        outputWriter.close();
    }
}
