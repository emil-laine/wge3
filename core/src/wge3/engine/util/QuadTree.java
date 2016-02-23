/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class QuadTree<E extends QuadTreeElement> {
    private static final int capacity = 8; // test different capacity values
    private static final int maxDepth = 5; // this is probably best, but might as well test changing this
    private static final int currentTree = -1;
    private static final int northEast = 0;
    private static final int northWest = 1;
    private static final int southWest = 2;
    private static final int southEast = 3;
    
    private final Rectangle region;
    private final int depth;
    private final List<E> elements;
    private final QuadTree<E> parent;
    private QuadTree<E>[] children; // order: NE, NW, SW, SE
    
    public QuadTree(float width, float height) {
        this(0, 0, width, height, 1, null);
    }
    
    private QuadTree(float x, float y, float width, float height, int depth, QuadTree parent) {
        this.region = new Rectangle(x, y, width, height);
        this.depth = depth;
        this.parent = parent;
        this.elements = new ArrayList(capacity);
    }
    
    public void add(E element) {
        if (elements.size() < capacity || depth == maxDepth) {
            elements.add(element);
            return;
        }
        
        if (!hasChildren())
            split();
        
        int index = getQuadrantIndex(element);
        
        if (index != currentTree)
            children[index].add(element);
        else
            elements.add(element);
    }
    
    public void remove(E element) {
        int index = getQuadrantIndex(element);
        
        if (index != currentTree && hasChildren())
            children[index].remove(element);
        else {
            elements.remove(element);
            if (depth > 1) parent.removeEmptyChildren();
        }
    }
    
    private void split() {
        children = new QuadTree[4];
        final float x = region.x, y = region.y;
        final float w = region.width / 2, h = region.height / 2;
        children[northEast] = new QuadTree(x+w, y+h, w, h, depth+1, this);
        children[northWest] = new QuadTree(x,   y+h, w, h, depth+1, this);
        children[southWest] = new QuadTree(x,   y,   w, h, depth+1, this);
        children[southEast] = new QuadTree(x+w, y,   w, h, depth+1, this);
    }
    
    public List<E> getElementsInside(Rectangle region) {
        final List<E> results = new ArrayList();
        appendElementsInside(region.x, region.y,
                region.x + region.width, region.y + region.height, results);
        return results;
    }
    
    private void appendElementsInside(final float leftX, final float bottomY,
                                      final float rightX, final float topY,
                                      List<E> results) {
        results.addAll(elements);
        
        if (!hasChildren()) return;
        
        List<QuadTree> toAppendFrom = new ArrayList(4);
        final float midX = getHorizontalMidpoint();
        final float midY = getVerticalMidpoint();
        
        if (leftX >= midX) {
            if (topY > midY)
                toAppendFrom.add(children[northEast]);
            if (bottomY < midY)
                toAppendFrom.add(children[southEast]);
        } else if (rightX <= midX) {
            if (topY > midY)
                toAppendFrom.add(children[northWest]);
            if (bottomY < midY)
                toAppendFrom.add(children[southWest]);
        } else {
            if (topY > midY) {
                toAppendFrom.add(children[northEast]);
                toAppendFrom.add(children[northWest]);
            }
            if (bottomY < midY) {
                toAppendFrom.add(children[southWest]);
                toAppendFrom.add(children[southEast]);
            }
        }
        
        for (QuadTree child : toAppendFrom) {
            child.appendElementsInside(leftX, bottomY, rightX, topY, results);
        }
    }
    
    public List<E> getElementsOverlapping(Rectangle region) {
        final List<E> results = new ArrayList();
        appendElementsOverlapping(region.x, region.y,
                region.x + region.width, region.y + region.height, results);
        return results;
    }
    
    private void appendElementsOverlapping(final float leftX, final float bottomY,
                                      final float rightX, final float topY,
                                      List<E> results) {
        results.addAll(elements);
        
        if (!hasChildren()) return;
        
        List<QuadTree> toAppendFrom = new ArrayList(4);
        final float midX = getHorizontalMidpoint();
        final float midY = getVerticalMidpoint();
        
//        if ()
    }
    
    private float getHorizontalMidpoint() {
        return region.x + region.width/2;
    }
    
    private float getVerticalMidpoint() {
        return region.y + region.height/2;
    }
    
    private boolean hasChildren() {
        return children != null;
    }
    
    /** Returns the index of the subtree where the given element belongs to, or
     *  an empty optional if it belongs to the current tree. */
    private int getQuadrantIndex(E element) {
        return getIndex(element.getLeftX(), element.getBottomY(),
                        element.getRightX(), element.getTopY());
    }
    
    private int getIndex(Rectangle bounds) {
        return getIndex(bounds.x, bounds.y,
                bounds.x + bounds.width, bounds.y + bounds.height);
    }
    
    private int getIndex(float leftX, float bottomY, float rightX, float topY) {
        final float midX = getHorizontalMidpoint();
        final float midY = getVerticalMidpoint();
        
        if (leftX >= midX) {
            if (bottomY >= midY) return northEast;
            else if (topY < midY) return southEast;
        } else if (rightX < midX) {
            if (bottomY >= midY) return northWest;
            else if (topY < midY) return southWest;
        }
        return currentTree;
    }
    
    public void updateElement(E element, Rectangle oldBounds) {
        // TODO: Optimize.
        removeOld(element, oldBounds);
        removeEmptyChildren();
        add(element);
    }
    
    private void removeOld(E element, Rectangle oldBounds) {
        final int index = getIndex(oldBounds);
        
        if (index != currentTree && hasChildren())
            children[index].removeOld(element, oldBounds);
        else {
            elements.remove(element);
            if (depth > 1) parent.removeEmptyChildren();
        }
    }
    
    // for debugging
    public void draw(Batch batch, ShapeRenderer sr) {
        sr.setColor(1, 0, 0, 1);
        
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.rect(region.x, region.y, region.width, region.height);
        for (E element : elements) {
            sr.rect(element.getLeftX(), element.getBottomY(),
                    element.getWidth(), element.getHeight());
        }
        sr.end();
        
        batch.begin();
        batch.enableBlending();
        Debug.getFont().draw(batch, String.valueOf(depth), region.x + (depth-1) * 8, region.y + region.height);
        for (E element : elements) {
            Debug.getFont().draw(batch, String.valueOf(depth), element.getRightX(), element.getBottomY());
        }
        batch.disableBlending();
        batch.end();
        
        if (hasChildren()) {
            for (QuadTree child : children) {
                child.draw(batch, sr);
            }
        }
    }
    
    private boolean isEmpty() {
        if (hasChildren()) {
            for (QuadTree child : children) {
                if (!child.isEmpty())
                    return false;
            }
        }
        return elements.isEmpty();
    }
    
    private void removeEmptyChildren() {
        if (!hasChildren()) return;
        
        boolean allChildrenAreEmpty = true;
        
        for (QuadTree child : children) {
            child.removeEmptyChildren();
            if (!child.isEmpty()) {
                allChildrenAreEmpty = false;
            }
        }
        
        if (allChildrenAreEmpty) children = null;
    }
}
