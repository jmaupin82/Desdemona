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
import java.awt.event.*;


public class Billy extends Frame implements Constants, ActionListener {
    /**
	 * 
	 */
    BoardGUI board;
    Panel buttons;
    Button first,last,prev,next;
    Canvas colorTag;
    Label black,white;
    int color;

    public static Billy topFrame;
  
	private static final long serialVersionUID = -2218510212246872379L;
	
	
	public BoardGUI getBoard() {
		return board;
	}

	public static void main(String args[]) {

        new Billy().go(args);
    }

    public void go(String args[]) {

        topFrame = this;

        addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            //System.exit(0);
            dispose();
            }
        });

        setLayout(new BorderLayout(0,0));

        board = new BoardGUI();

        add(board,BorderLayout.CENTER);

        first = new Button("<<");
        first.addActionListener(this);
        first.setActionCommand("first");

        prev = new Button("<");
        prev.addActionListener(this);
        prev.setActionCommand("prev");

        next = new Button(">");
        next.addActionListener(this);
        next.setActionCommand("next");

        last = new Button(">>");
        last.addActionListener(this);
        last.setActionCommand("last");

        black = new Label("(2)");
        black.setForeground(Color.black);
        white = new Label("(2)");
        white.setForeground(Color.white);

        colorTag = new Canvas();
        colorTag.setPreferredSize(new Dimension(15,15));

        buttons = new Panel(new FlowLayout(FlowLayout.LEFT,1,1));
        buttons.add(first);
        buttons.add(prev);
        buttons.add(next);
        buttons.add(last);
        buttons.add(black);
        buttons.add(new Label("/"));
        buttons.add(white);
        buttons.add(new Label("next to play:"));
        buttons.add(colorTag);

        add(buttons,BorderLayout.NORTH);

        pack();
        setVisible(true);

        String title = "Billy";

        if (args.length>0) {
            try {
                title += " - " + args[0];
                board.readInputFile(args[0]);
            } catch(Exception e) {
                new MessageDialog(this,e.getMessage(),true).setVisible(true);
            }
        }

        setTitle(title);

        updateStates();

        validate();

    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("first")) {
            board.first();
        } else if(e.getActionCommand().equals("prev")) {
            board.prev();
        } else if(e.getActionCommand().equals("next")) {
            board.next();
        } else if(e.getActionCommand().equals("last")) {
            board.last();
        } else {
            System.err.println("unknown action command!");
            return;
        }
        updateStates();
    }

    public void message(String msg) {
        new MessageDialog((Frame)getParent(),msg,true).setVisible(true);
    }

    public void updateStates() {
        boolean p = board.history.hasPrev();
        boolean n = board.history.hasNext();
        first.setEnabled(p);
        prev.setEnabled(p);
        next.setEnabled(n);
        last.setEnabled(n);
        color = nextColor();
        if(!board.findPossible(color))
            if (board.findPossible(-color))
                color = -color;
        black.setText("("+board.count(BLACK)+")");
        white.setText("("+board.count(WHITE)+")");
        buttons.validate();
        colorTag.setBackground(color==BLACK?Color.black:Color.white);
        colorTag.repaint();
    }

    public int nextColor() {
        return board.history.size() == 0 || !board.history.hasPrev() || board.history.getLastColor()==WHITE ? BLACK : WHITE;

    }

    public void makeMove(Square square) {
        if (square.isClickable()) {
        	System.out.println("billy clickable");
        	board.play(color,square.getXCoord(),square.getYCoord());
        }
        
        updateStates();
    }

  public MouseAdapter mouseListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
        	System.out.println((Square)e.getComponent());
            makeMove((Square)e.getComponent());
        }
    };
}
