
import java.util.Iterator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ryan W Radtke <RyanWRadtke@gmail.com>
 * @param <T>
 */
public interface IteratorInterface<T> extends Iterator<T> {
    @Override
    public boolean hasNext();
    @Override
    public T next();
    @Override
    public void remove();
}
