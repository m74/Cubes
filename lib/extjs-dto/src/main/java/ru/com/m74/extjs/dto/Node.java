package ru.com.m74.extjs.dto;

public interface Node<T extends Node, I> {
    I getId();

    I getParentId();

    void setChildren(Iterable<T> nodes);

    Iterable<T> getChildren();
}
