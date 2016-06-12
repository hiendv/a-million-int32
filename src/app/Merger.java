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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

/**
 *
 * @author hiendv
 */
public class Merger implements app.contracts.Merger {

    protected File[] files;
    protected Writer writer;

    /**
     * Merge chunks
     * @param files
     * @param writer
     */
    public Merger(File[] files, Writer writer) {
        this.files = files;
        this.writer = writer;
    }

    /**
     * Find the smallest integer
     * @param heap
     * @param skips
     * @param lastPick
     * @return
     */
    protected int findMin(int[] heap, long[] skips, int lastPick) {
        if (lastPick == -1 || skips[lastPick] == -1) {
            // lastPick is not set or belongs to a "dead" chunk
            // Get a new one
            for (int i = 0; i < heap.length; i++) {
                if (skips[i] == -1) {
                    continue;
                }
                if (skips[i] >= 0) {
                    lastPick = i;
                }
            }
        }

        int min = heap[lastPick];

        for (int i = 0; i < heap.length; i++) {
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

        return lastPick;
    }

    @Override
    public void merge() throws IOException {
        int[] heap = new int[this.files.length];
        long[] skips = new long[this.files.length];
        int finishedCount = 0;
        int lastPick = -1;
        while (finishedCount < heap.length) {
            for (int i = 0; i < this.files.length; i++) {
                // If the chunk runs out of elements, skip it
                if (skips[i] == -1) {
                    // "Dead" chunk
                    continue;
                }

                // If the last pick is not the index
                // and not the initial value
                // and the related chunk is not dead
                if (lastPick != i
                    && lastPick != -1
                    && skips[lastPick] != -1) {
                    continue;
                }

                FileInputStream inputStream = new FileInputStream(files[i]);
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

                if (lastPick == -1 && i < heap.length - 1) {
                    // The heap is not completely loaded
                    continue;
                }

                // Find the minimum value
                lastPick = this.findMin(heap, skips, lastPick);
                String min = heap[lastPick] + "\n";
                // Write it out and update the skips[lastPick]
                writer.write(min);
                skips[lastPick] += min.length();
                writer.flush();
            }
        }
        writer.close();
    }
}
