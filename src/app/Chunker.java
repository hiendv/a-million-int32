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
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author hiendv
 */
public class Chunker implements app.contracts.Chunker {

    /**
     * Input file
     */
    protected String input;
    
    /**
     * Output directory
     */
    protected String output;
    
    /**
     * The length of each chunk
     */
    protected int capacity;
    
    /**
     * The chunk
     */
    protected int[] chunk;
    
    /**
     * IO
     */
    protected FileInputStream inputStream;
    protected Scanner scanner;
    protected BufferedWriter outputWriter;

    /**
     * Constructor
     * @param input
     * @param output
     * @param capacity
     * @throws FileNotFoundException
     */
    public Chunker(String input, String output, int capacity) throws FileNotFoundException {
        this.input = input;
        this.output = output;
        this.capacity = capacity;
        this.chunk = new int[this.capacity];
        this.setUpIO();
    }

    private void setUpIO() throws FileNotFoundException {
        this.inputStream = new FileInputStream(this.input);
        this.scanner = new Scanner(inputStream, "UTF-8");
        File outputDirectory = new File(this.output);
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }
    }
    
    /**
     * Write the chunk using BufferedWriter
     * @param list
     * @param chunkCount
     * @throws IOException
     */
    protected void write(int[] list, int chunkCount) throws IOException {
        this.outputWriter = new BufferedWriter(
            new FileWriter(this.output +"/"+ chunkCount)
        );
        for (int k : list) {
            outputWriter.write(k + "\n");
        }
        outputWriter.flush();
        outputWriter.close();
    }
    
    /**
     * Process a chunk
     * @param list
     * @param chunkCount
     * @return chunkCount which is updated
     * @throws IOException
     */
    protected int processChunk(int[] list, int chunkCount) throws IOException {
        Arrays.sort(list);
        write(list, chunkCount);
        return chunkCount + 1;
    }
    
    /**
     * Chunk the input data
     * @return the number of chunks
     * @throws IOException
     */
    @Override
    public int chunk() throws IOException {
        /**
         * Initialize chunkCount
         */
        int chunkCount = 0;
        
        int i = 0;
        while (this.scanner.hasNextLine()) {
            // For every line
            // Parse the line as an integer
            // And save it to the array to process the whole chunk later
            this.chunk[i] = Integer.parseInt(this.scanner.nextLine());
            i++;
            if (i == this.capacity) {
                // Limit reached
                chunkCount = processChunk(this.chunk, chunkCount);
                // Reset index to 0
                i = 0;
            }
        }
        
        if (i > 0 && i < this.capacity) {
            // Check for the left chunk existence
            chunkCount = processChunk(Arrays.copyOfRange(this.chunk, 0, i), chunkCount);
        }
        
        return chunkCount;
    }
}
