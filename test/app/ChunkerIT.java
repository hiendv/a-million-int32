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
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
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
        this.generator = new Random();
        this.count = 50000;
        this.input = Seeder.seed(this.count, "dataset");
    }

    @After
    public void tearDown() {
    }

    /**
     * Test if the number of chunks correct
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testChunkCount() throws Exception {
        System.out.println("testChunkCount");
        int cap = generator.nextInt(10000);
        Chunker instance = new Chunker(
            new FileInputStream(this.input),
            cap
        );
        File[] result = instance.chunk();
        assertEquals((int) Math.ceil((double) this.count / cap), result.length);
    }
    
    /**
     * Test if the number of lines in chunked files equal to the original number of int
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testTotalLine() throws Exception {
        System.out.println("testTotalLine");
        int cap = generator.nextInt(10000);
        Chunker instance = new Chunker(
            new FileInputStream(this.input),
            cap
        );
        File[] result = instance.chunk();
        LineNumberReader reader = null;
        int lineCount = 0;
        for (File f : result) {
            try {
                reader = new LineNumberReader(new FileReader(f));
                while ((reader.readLine()) != null);
                lineCount += reader.getLineNumber();
            } catch (Exception ex) {
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
        assertEquals(lineCount, this.count);
    }

}
