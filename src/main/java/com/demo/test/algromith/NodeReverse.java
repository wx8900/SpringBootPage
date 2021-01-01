package com.demo.test.algromith;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Jack
 */
public class NodeReverse {

    public static void main(String[] args) {
        Node node4 = new Node(4, null);
        Node node3 = new Node(3, node4);
        Node node2 = new Node(2, node3);
        Node node1 = new Node(1, node2);
        System.out.println(reverse(node1));
    }

    private static Node reverse(Node cur) {
        if (cur == null) {
            return null;
        }
        // 1->2->3->4->null
        // cur
        // null<-1<-2<-3<-4
        // cur do 4 things:
        // 1. set tail default is null, cache the next point position
        // 2. update next point to tail
        // 3. update tail to cur
        // 4. update cur to cache
        // return tail (not cur, return cur will null)
        Node tail = null;
        while (cur != null) {
            Node tmp = cur.next;
            cur.next = tail;
            tail = cur;
            cur = tmp;
        }
        return tail;
    }

}

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class Node {
    int value;
    Node next;
}
