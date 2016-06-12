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
import java.io.PrintWriter;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hiendv
 */
public class ChunkerIT {

    protected int count;
    protected File input;
    protected Random generator;

    public ChunkerIT() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws IOException {
        this.input = File.createTempFile("dataset", "tmp");
        this.input.deleteOnExit();
        
        PrintWriter writer = new PrintWriter(this.input, "UTF-8");
        this.generator = new Random();
        this.count = 50000;
        StringBuilder stringBuilder = new StringBuilder();
        int[] numbers = Seeder.seed(this.count);
        for (int i : numbers) {
            writer.println(i);
        }
        writer.close();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of chunk method, of class Chunker.
     * @throws java.lang.Exception
     */
    @Test
    public void testChunk() throws Exception {
        System.out.println("chunk");
        int cap = generator.nextInt(10000);
        Chunker instance = new Chunker(
            new FileInputStream(this.input),
            cap
        );
        File[] result = instance.chunk();
        assertEquals((int) Math.ceil((double) this.count/cap), result.length);
    }

}
