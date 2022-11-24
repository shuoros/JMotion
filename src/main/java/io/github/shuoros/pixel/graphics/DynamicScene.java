package io.github.shuoros.pixel.graphics;

import java.util.Collection;

public abstract class DynamicScene implements Scene {

    public abstract Map map();

    public void renderBoard() {
        map().getTiles()
                .stream()
                .flatMap(Collection::stream)
                .toList()
                .forEach(Tile::render);
    }

}
