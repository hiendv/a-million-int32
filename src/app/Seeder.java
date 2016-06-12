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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 *
 * @author hiendv
 */
public class Seeder {

    public static int[] seed(int length) {
        int[] list = new int[length];
        Random generator = new Random();
        for (int i = 0; i < length; i++) {
            list[i] = generator.nextInt();
        }
        return list;
    }

    public static File seed(int length, String name) throws IOException {
        File file = File.createTempFile(name, ".tmp");
        return Seeder.seedFile(length, file);
    }

    public static File seedFile(int length, File file) throws IOException {
        PrintWriter writer = new PrintWriter(file, "UTF-8");
        Seeder
            .seedWriter(length, writer)
            .close();
        return file;
    }

    public static PrintWriter seedWriter(int length, PrintWriter writer) throws IOException {
        for (int i : Seeder.seed(length)) {
            writer.println(i);
        }
        return writer;
    }

}
