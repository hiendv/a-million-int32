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

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hiendv
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String[] options = {"input", "output", "capacity"};
        if (args.length != 3) {
            System.out.println("Arguments: "+Arrays.toString(options));
            return;
        }
        options = args.clone();
        App app = new App();
        app.setInput(options[0]);
        app.setOutput(options[1]);
        app.setCap(Integer.parseInt(options[2]));
        try {
            app.init();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
