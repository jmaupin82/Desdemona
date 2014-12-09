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

public interface Constants {
    public static final int SIZE = 10;

    // states
    public static final int EMPTY = 0;
    public static final int BLACK = -1;
    public static final int WHITE = +1;

    public static final Color darkGreen = new Color(0,127,0);

    public static final int increment[] = { -1,0,+1};

    // This is the number of plies that the minimax agent will
    // examine while training Iago.
    public static final int numPliesIago = 2;
}
