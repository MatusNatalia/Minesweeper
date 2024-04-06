package ru.cft.focus.view;

import ru.cft.focus.model.Cell;

public final class CellToImageConverter {

    private CellToImageConverter() {
    }

    public static GameImage convertCellToImage(Cell cell) {
        switch (cell.getCellState()) {
            case CLOSED -> {
                return GameImage.CLOSED;
            }
            case MARKED -> {
                return GameImage.MARKED;
            }
            case OPENED -> {
                if (cell.isMined()) {
                    return GameImage.BOMB;
                }
                switch (cell.getBombsAround()) {
                    case 0 -> {
                        return GameImage.EMPTY;
                    }
                    case 1 -> {
                        return GameImage.NUM_1;
                    }
                    case 2 -> {
                        return GameImage.NUM_2;
                    }
                    case 3 -> {
                        return GameImage.NUM_3;
                    }
                    case 4 -> {
                        return GameImage.NUM_4;
                    }
                    case 5 -> {
                        return GameImage.NUM_5;
                    }
                    case 6 -> {
                        return GameImage.NUM_6;
                    }
                    case 7 -> {
                        return GameImage.NUM_7;
                    }
                    case 8 -> {
                        return GameImage.NUM_8;
                    }
                    default -> throw new UnsupportedOperationException();
                }
            }
            default -> throw new UnsupportedOperationException();
        }
    }

}
