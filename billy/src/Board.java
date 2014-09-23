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
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

// interface and memory representation are linked, hence that is bad design. do what i say, not what i do.

public class Board extends Panel implements Constants {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7165735878597921168L;

	private Square position[][];

    public History history = new History();
    
    public Board() {
        setLayout(new GridLayout(SIZE,SIZE,1,1));
        setBackground(Color.BLACK);
        position = new Square[SIZE][SIZE];
        for (int i=0;i<SIZE;i++) {
            for (int j=0;j<SIZE;j++) {
                Square sq = new Square(i,j);
                position[i][j]=sq;
                add(sq,i*SIZE+j);
            }
        }

        initialPosition();
    }

    private void initialPosition() {
        for (int i=0;i<SIZE;i++)
            for (int j=0;j<SIZE;j++) {
                if (i==4 && j==4 || i==5 && j==5){
                	setState(i,j,WHITE);
                }
                else if (i==4 && j==5 || i==5 && j==4){
                	setState(i,j,BLACK);
                }
                else {
                	setState(i,j,EMPTY);
                }
            }
    }

    public Square getSquare(int x,int y) {
        return position[y][x];
    }

    public int getState(int x,int y) {
        return position[y][x].getState();
    }

    public void setState(int x,int y,int state) {
        position[y][x].setState(state);
    }

    public void readInputFile(String filename) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String content="",line;
        while ((line=reader.readLine())!=null) content+=line;

        Pattern move = Pattern.compile("(b|w)\\[([a-j])([a-j])\\]"); // will skip pass moves
        Matcher match = move.matcher(content);
        while (match.find()) {
            int c = match.group(1).equals("b")?BLACK:WHITE;
            int x = (int)match.group(2).charAt(0) - (int)'a';
            int y = (int)match.group(3).charAt(0) - (int)'a';
            history.add(c,new Point(x,y));
        }
        
        reader.close();
    }

    public void first() {
        while(history.hasPrev()) prev();
    }

    public void prev() {
        Point m = history.getLastMove();
        position[(int)m.getY()][(int)m.getX()].setState(EMPTY);
        List<Point> changed =  history.takeBack();
        for(Point p:changed) position[(int)p.getY()][(int)p.getX()].flip();
    }

    public void next() {
        int c = history.getNextColor();
        List<Point> ch = impact(c,history.getNextMove());
        Point m = history.getNextMove();
        history.forward(ch);
        position[(int)m.getY()][(int)m.getX()].setState(c);
        for(Point p:ch) position[(int)p.getY()][(int)p.getX()].flip();
    }

    public void last() {
        while (history.hasNext()) { 
        	next();
        }
    }

    public void play(int color,int x,int y) {
        Point m = new Point(x,y);
        List<Point> ch = impact(color,m);
        history.play(color,m,ch);
        setState(x,y,color);
        for(Point p:ch) {
        	position[(int)p.getY()][(int)p.getX()].flip();
        }
    }

    public boolean findPossible(int color) {
        boolean found = false;

        List<Point> list;

        for(int x=0;x<SIZE;x++)
            for(int y=0;y<SIZE;y++) {
                if (getState(x,y)==EMPTY) {
                    list = impact(color,new Point(x,y));
                    if (list.size()>0) {
                        found = true;
                        getSquare(x,y).setClickable(true);
                        continue;
                    }
                }
                getSquare(x,y).setClickable(false);
            }

        return found;
    }

    public int count(int color) {
        int rst = 0;
        for(int x=0;x<SIZE;x++)
            for(int y=0;y<SIZE;y++)
                if (getState(x,y) == color){
                	rst++;
                }
        return rst;
    }

    private List<Point> impact(int color,Point move) {
        List<Point> rst = new ArrayList<Point>();
        for (int i:increment)
            for(int j:increment) {
                if (i==0 && j==0) continue;
                List<Point> line = lineImpact(color,(int)move.getX(),(int)move.getY(),i,j);
                if (line != null) rst.addAll(line);
            }
        return rst;
    }

    private List<Point> lineImpact(int color,int x,int y,int dx,int dy) {
        List<Point> rst = new ArrayList<Point>();
        x+=dx; y+=dy;
        while (x>=0 && x<SIZE && y>=0 && y<SIZE) {
            if (getState(x,y)==-color) rst.add(new Point(x,y));
            else if (getState(x,y)==EMPTY) return null;
            else if (getState(x,y)==color) return rst;
            x+=dx; y+=dy;
        }
        return null;
    }
}
