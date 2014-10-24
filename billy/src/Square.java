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

public class Square extends Canvas implements Constants {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1177057521102825598L;
	public Square(int i,int j) {
        setBackground(darkGreen);
        state=EMPTY;
        x=i;
        y=j;
        addMouseListener(Billy.topFrame.mouseListener);
        disableEvents(AWTEvent.MOUSE_EVENT_MASK);
        clickable = false;
    }

    public void paint(Graphics g) {
        if(state!=EMPTY) {
            g.setColor(state==WHITE?Color.white:Color.black);
            Dimension size = getSize();
            if (size.width<4 || size.height<4) return;
            g.fillOval(2,2,size.width-4,size.height-4);
        }
    }

    public int getXCoord() { return y; }
    public int getYCoord() { return x; }

    public int getState() { return state; }

    public void setState(int s) { if (s!=state) { state = s; repaint(); } }

    public void flip() { assert(state!=EMPTY); state = (state==BLACK?WHITE:BLACK); repaint(); }

    public Dimension getPreferredSize() {
        return new Dimension(50,50);
    }

    public boolean isClickable() { return clickable; }

    public void setClickable(boolean c) {
        clickable = c;
        if(clickable) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        }
        else {
            setCursor(Cursor.getDefaultCursor());
            disableEvents(AWTEvent.MOUSE_EVENT_MASK);
        }
    }

    protected boolean clickable;
    protected int state;
    protected int x;
    protected int y;
    
    public String toString() {
    	return "Square x =" + x + " y = " + y + " state= " + state;
    }
 
}
