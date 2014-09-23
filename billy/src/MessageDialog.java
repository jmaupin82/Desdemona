/**
 * Billy - free 10x10 Othello Board
 *
 * Copyright (C) Claude BRISSON <claude@renegat.net>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */

import java.awt.*;

public class MessageDialog extends Dialog {
  private Panel text = new Panel();  // panel med Labels

  public MessageDialog(Frame parent, String m, boolean modal) {
    super(parent, modal);
    initText(m);
  }
/*
  public MessageDialog(Dialog parent, String m, boolean modal) {
    super(parent, "", modal);  // Java 1.2
    initText(m);
  }
*/
  private void initText(String m) {
    text.setLayout(new GridLayout(0,1));  // 1 kolumn, godtyckligt många rader
    add("Center", text);
    int i=0, j;   // index för första och sista teckent på en rad
    // plocka ut en rad i taget ur meddelandet
    while ((j=m.indexOf('\n', i)) >= 0) {
      text.add(new Label(m.substring(i,j), Label.CENTER));  // nästa rad
      i = j+1;
    }
    text.add(new Label(m.substring(i), Label.CENTER));  // sista raden
    pack();
  }

  public void setTextFont(Font f) {
    text.setFont(f);
    pack();
  }
}

