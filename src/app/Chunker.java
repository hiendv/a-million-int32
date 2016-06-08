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

    protected String input;
    protected String output;
    protected int capacity;
    protected int[] x;
    protected FileInputStream inputStream;
    protected Scanner scanner;
    protected BufferedWriter outputWriter;

    public Chunker(String input, String output, int capacity) throws FileNotFoundException {
        this.input = input;
        this.output = output;
        this.capacity = capacity;
        this.x = new int[this.capacity];
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
    
    protected void write(int chunks) throws IOException {
        this.outputWriter = new BufferedWriter(
            new FileWriter(this.output +"/"+ chunks)
        );
        for (int k : x) {
            outputWriter.write(k + "\n");
        }
        outputWriter.flush();
        outputWriter.close();
    }
    
    protected int processChunk(int chunkCount) throws IOException {
        Arrays.sort(x);
        write(chunkCount);
        return chunkCount + 1;
    }
    
    @Override
    public int chunk() throws IOException {
        int chunkCount = 0;
        int i = 0;
        while (this.scanner.hasNextLine()) {
            this.x[i] = Integer.parseInt(this.scanner.nextLine());
            i++;
            if (i == this.capacity) {
                chunkCount = processChunk(chunkCount);
                i = 0;
            }
        }
        if (i > 0 && i < this.capacity) {
            chunkCount = processChunk(chunkCount);
        }
        return chunkCount;
    }
}
