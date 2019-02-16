/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

/**
 *
 * @author user
 */
public class Example {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        I2CDevice _device = null;
        I2CLCD _lcd = null;

        try {
            I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
            _device = bus.getDevice(0x27);
            _lcd = new I2CLCD(_device);
            _lcd.init();
            _lcd.backlight(true);
            _lcd.display_string_pos("Hello, world!", 1, 2);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

}
