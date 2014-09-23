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

import java.util.List;
import java.util.ArrayList;
import java.awt.Point;

public class History {

    public History() {
        current = -1;
    }

    public void add(int c,Point m) {
        moves.add(new Step(c,m));
    }

    public void play(int color,Point m,List<Point> ch) {
        if(moves.size()>current+1) {
            assert(color == getLastColor());
            if (!m.equals(moves.get(current+1).move)) {
                while(moves.size()>current+1) moves.remove(current+1);
                moves.add(new Step(color,m,ch));
            }
            // trust the newcomer
            moves.get(current+1).changed = ch;
        }
        else moves.add(new Step(color,m,ch));
        current++;
    }

    public void forward(List<Point> ch) {
        assert(moves.size()>current+1);
        current++;
        moves.get(current).changed = ch;
    }

    public boolean hasPrev() { return current >= 0;}

    public boolean hasNext() { return current < moves.size()-1;}

    public int size() { return moves.size(); }

    public List<Point> takeBack() {
        assert(current >=0 && moves.get(current).changed != null);
        return moves.get(current--).changed;
    }

    public int getLastColor() { assert(current>=0); return moves.get(current).color; }

    public Point getLastMove() { assert(current>=0); return moves.get(current).move; }

    public int getNextColor() { assert(moves.size()>current+1); return moves.get(current+1).color;}

    public Point getNextMove() { assert(moves.size()>current+1); return moves.get(current+1).move;}

    class Step {
        public Step(int c,Point m) { color = c; move = m; changed = null;}
        public Step(int c,Point m,List ch) { color = c; move = m; changed = ch; }
        public int color;
        public Point move;
        public List<Point> changed;
    }

    List<Step> moves = new ArrayList<Step>();
    int current;
}
