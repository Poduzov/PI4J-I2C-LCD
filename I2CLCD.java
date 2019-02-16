/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import com.pi4j.io.i2c.I2CDevice;

/**
 *
 * @author user
 */
public class I2CLCD {

    private I2CDevice _device;

    public I2CLCD(I2CDevice device) {
        _device = device;
    }

// Write a single command
    private void write_cmd(byte cmd) {
        try {
            _device.write(cmd);
            Thread.sleep(0, 100000);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Write a command and argument
    private void write_cmd_arg(byte cmd, byte[] data) {
        try {
            _device.write(cmd, data);
            Thread.sleep(0, 100000);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Write a block of data
    private void write_block_data(byte cmd, byte[] data) {
        try {
            _device.write(cmd, data);
            Thread.sleep(0, 100000);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Read a single byte def
    private byte read() {
        try {
            return (byte) _device.read();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return (byte) 0;
    }

    // Read
    private byte[] read_data(byte cmd) {
        byte[] buffer = new byte[cmd];
        try {
            _device.read(buffer, 0, cmd);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return buffer;
    }

    // Read a block of data
    private byte[] read_block_data(byte cmd) {
        byte[] buffer = new byte[cmd];
        try {
            _device.read(buffer, 0, cmd);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return buffer;
    }

    private final byte LCD_CLEARDISPLAY = (byte) 0x01;
    private final byte LCD_RETURNHOME = (byte) 0x02;
    private final byte LCD_ENTRYMODESET = (byte) 0x04;
    private final byte LCD_DISPLAYCONTROL = (byte) 0x08;
    private final byte LCD_CURSORSHIFT = (byte) 0x10;
    private final byte LCD_FUNCTIONSET = (byte) 0x20;
    private final byte LCD_SETCGRAMADDR = (byte) 0x40;
    private final byte LCD_SETDDRAMADDR = (byte) 0x80;

    // flags for display entry mode 
    private final byte LCD_ENTRYRIGHT = (byte) 0x00;
    private final byte LCD_ENTRYLEFT = (byte) 0x02;
    private final byte LCD_ENTRYSHIFTINCREMENT = (byte) 0x01;
    private final byte LCD_ENTRYSHIFTDECREMENT = (byte) 0x00;

    // flags for display on/off control
    private final byte LCD_DISPLAYON = (byte) 0x04;
    private final byte LCD_DISPLAYOFF = (byte) 0x00;
    private final byte LCD_CURSORON = (byte) 0x02;
    private final byte LCD_CURSOROFF = (byte) 0x00;
    private final byte LCD_BLINKON = (byte) 0x01;
    private final byte LCD_BLINKOFF = (byte) 0x00;

    // flags for display/cursor shift
    private final byte LCD_DISPLAYMOVE = (byte) 0x08;
    private final byte LCD_CURSORMOVE = (byte) 0x00;
    private final byte LCD_MOVERIGHT = (byte) 0x04;
    private final byte LCD_MOVELEFT = (byte) 0x00;

    // flags for function set
    private final byte LCD_8BITMODE = (byte) 0x10;
    private final byte LCD_4BITMODE = (byte) 0x00;
    private final byte LCD_2LINE = (byte) 0x08;
    private final byte LCD_1LINE = (byte) 0x00;
    private final byte LCD_5x10DOTS = (byte) 0x04;
    private final byte LCD_5x8DOTS = (byte) 0x00;

    // flags for backlight control
    private final byte LCD_BACKLIGHT = (byte) 0x08;
    private final byte LCD_NOBACKLIGHT = (byte) 0x00;

    private final byte En = (byte) 0b00000100; // Enable bit
    private final byte Rw = (byte) 0b00000010; // Read/Write bit
    private final byte Rs = (byte) 0b00000001; // Register select bit

    //initializes objects and lcd
    public void init() {
        try {
            lcd_write((byte) 0x03);
            lcd_write((byte) 0x03);
            lcd_write((byte) 0x03);
            lcd_write((byte) 0x02);

            lcd_write((byte) (LCD_FUNCTIONSET | LCD_2LINE | LCD_5x8DOTS | LCD_4BITMODE));
            lcd_write((byte) (LCD_DISPLAYCONTROL | LCD_DISPLAYON));
            lcd_write((byte) (LCD_CLEARDISPLAY));
            lcd_write((byte) (LCD_ENTRYMODESET | LCD_ENTRYLEFT));
            Thread.sleep(0, 200000);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    // clocks EN to latch command
    private void lcd_strobe(byte data) {
        try {
            _device.write((byte) (data | En | LCD_BACKLIGHT));
            Thread.sleep(0, 500000);
            _device.write((byte) ((data & ~En) | LCD_BACKLIGHT));
            Thread.sleep(0, 100000);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void lcd_write_four_bits(byte data) {
        try {
            _device.write((byte) (data | LCD_BACKLIGHT));
            lcd_strobe(data);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void lcd_write(byte cmd, byte mode) {
        lcd_write_four_bits((byte) (mode | (cmd & 0xF0)));
        lcd_write_four_bits((byte) (mode | ((cmd << 4) & 0xF0)));
    }

    // write a command to lcd
    private void lcd_write(byte cmd) {
        lcd_write(cmd, (byte) 0);
    }

    // write a character to lcd
    public void write_char(byte charvalue) {
        byte mode = 1;
        lcd_write_four_bits((byte) (mode | (charvalue & 0xF0)));
        lcd_write_four_bits((byte) (mode | ((charvalue << 4) & 0xF0)));
    }

    // put string function
    public void display_string(String string, int line) {
        switch (line) {
            case 1:
                lcd_write((byte) 0x80);
                break;
            case 2:
                lcd_write((byte) 0xC0);
                break;
            case 3:
                lcd_write((byte) 0x94);
                break;
            case 4:
                lcd_write((byte) 0xD4);
                break;
        }

        for (int i = 0; i < string.length(); i++) {
            lcd_write((byte) string.charAt(i), Rs);
        }
    }

    // clear lcd and set to home
    private void clear() {
        lcd_write((byte) LCD_CLEARDISPLAY);
        lcd_write((byte) LCD_RETURNHOME);

    }

    // define backlight on / off(lcd.backlight(1) off = lcd.backlight(0)
    public void backlight(boolean state) {
        //for state, 1 = on, 0 = off
        if (state) {
            write_cmd(LCD_BACKLIGHT);

        } else {
            write_cmd(LCD_NOBACKLIGHT);
        }
    }

    // add custom characters(0 - 7)
    private void load_custom_chars(byte[][] fontdata) {

        lcd_write((byte) 0x40);
        for (int i = 0; i < fontdata.length; i++) {
            for (int j = 0; j < fontdata[i].length; j++) {
                write_char(fontdata[i][j]);
            }
        }
    }

    // define precise positioning (addition from the forum)
    public void display_string_pos(String string, int line, int pos) {
        byte pos_new = 0;

        if (line == 1) {
            pos_new = (byte) pos;
        } else if (line == 2) {
            pos_new = (byte) (0x40 + pos);
        } else if (line == 3) {
            pos_new = (byte) (0x14 + pos);
        } else if (line == 4) {
            pos_new = (byte) (0x54 + pos);
        }

        lcd_write((byte) (0x80 + pos_new));

        for (int i = 0; i < string.length(); i++) {
            lcd_write((byte) string.charAt(i), Rs);
        }
    }
}
