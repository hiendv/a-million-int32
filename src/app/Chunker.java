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
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author hiendv
 */
public class Chunker implements app.contracts.Chunker {

    /**
     * The length of each chunk
     */
    protected int capacity;

    /**
     * The chunk
     */
    protected int[] chunk;

    protected final Scanner scanner;

    /**
     * Constructor
     *
     * @param in
     * @param capacity
     * @throws FileNotFoundException
     */
    public Chunker(InputStream in, int capacity) throws FileNotFoundException {
        this.scanner = new Scanner(in, "UTF-8");
        this.capacity = capacity;
        this.chunk = new int[this.capacity];
    }

    /**
     * Write the chunk using writer
     *
     * @param chunk
     * @param writer
     * @throws IOException
     */
    protected void write(int[] chunk, Writer writer) throws IOException {
        for (int i : chunk) {
            writer.write(i + "\n");
        }
        writer.flush();
        writer.close();
    }

    /**
     * Process a chunk
     *
     * @param chunk
     * @return chunkCount which is updated
     * @throws IOException
     */
    protected File processChunk(int[] chunk) throws IOException {
        Arrays.sort(chunk);
        File temp = this.createTempFile();
        write(chunk, new BufferedWriter(
            new FileWriter(temp)
        ));
        return temp;
    }

    /**
     * Temporary file creation
     *
     * @return File
     * @throws IOException
     */
    protected File createTempFile() throws IOException {
        return File.createTempFile("chunk", ".tmp");
    }

    /**
     * Chunk the input data
     *
     * @return the number of chunks
     * @throws IOException
     */
    @Override
    public File[] chunk() throws IOException {
        int chunkCount = 0;
        List<File> files = new ArrayList<>();

        int i = 0;
        while (this.scanner.hasNextLine()) {
            // For every line
            // Parse the line as an integer
            // And save it to the array to process the whole chunk later
            this.chunk[i] = Integer.parseInt(this.scanner.nextLine());
            i++;
            if (i == this.capacity) {
                // Limit reached
                // Add the created file
                files.add(
                    this.processChunk(this.chunk)
                );
                chunkCount++;
                // Reset index to 0
                i = 0;
            }
        }

        if (i > 0 && i < this.capacity) {
            // Check for the left chunk existence
            files.add(
                this.processChunk(
                    Arrays.copyOfRange(this.chunk, 0, i)
                )
            );
            chunkCount++;
        }

        return files.toArray(new File[chunkCount]);
    }
}
