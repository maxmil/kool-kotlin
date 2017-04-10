package my.favorite.kotlin.features;

class Box<T> {

    void tryCovariantAssign(Box<Integer> boxOfInteger) {
        Box<? extends Number> boxOfNumber = boxOfInteger;
    }
}