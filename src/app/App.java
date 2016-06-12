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

/**
 *
 * @author hiendv
 */
public class App {

    protected int cap;
    protected File[] files;
    protected String input;
    protected String output;
    protected app.contracts.Chunker chunker;
    protected app.contracts.Merger merger;

    public void init() throws IOException {
        this.chunker = new Chunker(new FileInputStream(this.input), this.cap);
        this.files = chunker.chunk();
        this.merger = new Merger(files, new BufferedWriter(
            new FileWriter(this.output)
        ));
        this.merger.merge();
    }

    public void setCap(int cap) {
        this.cap = cap;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
