package com.demo.test.algromith;

import java.util.ArrayList;
import java.util.List;

/**
 * 834. 树中距离之和
 * 给定一个无向、连通的树。树中有 N 个标记为 0...N-1 的节点以及 N-1 条边 。
 * 第 i 条边连接节点 edges[i][0] 和 edges[i][1] 。
 * 返回一个表示节点 i 与其他所有节点距离之和的列表 ans。
 * <p>
 * 示例 1:
 * 输入: N = 6, edges = [[0,1],[0,2],[2,3],[2,4],[2,5]]
 * 输出: [8,12,6,10,10,10]
 * 解释:
 * 如下为给定的树的示意图：
 * 0
 * / \
 * 1   2
 * /|\
 * 3 4 5
 * 我们可以计算出 dist(0,1) + dist(0,2) + dist(0,3) + dist(0,4) + dist(0,5)
 * 也就是 1 + 1 + 2 + 2 + 2 = 8。 因此，answer[0] = 8，以此类推。
 */
public class Solution0126 {
    int[] ans;
    int[] sz;
    int[] dp;
    List<List<Integer>> graph;

    public int[] sumOfDistancesInTree(final int N, final int[][] edges) {
        this.ans = new int[N];
        this.sz = new int[N];
        this.dp = new int[N];
        this.graph = new ArrayList<List<Integer>>();
        for (int i = 0; i < N; ++i) {
            this.graph.add(new ArrayList<Integer>());
        }
        for (final int[] edge : edges) {
            final int u = edge[0];
            final int v = edge[1];
            this.graph.get(u).add(v);
            this.graph.get(v).add(u);
        }
        this.dfs(0, -1);
        this.dfs2(0, -1);
        return this.ans;
    }

    public void dfs(final int u, final int f) {
        this.sz[u] = 1;
        this.dp[u] = 0;
        for (final int v : this.graph.get(u)) {
            if (v == f) {
                continue;
            }
            this.dfs(v, u);
            this.dp[u] += this.dp[v] + this.sz[v];
            this.sz[u] += this.sz[v];
        }
    }

    public void dfs2(final int u, final int f) {
        this.ans[u] = this.dp[u];
        for (final int v : this.graph.get(u)) {
            if (v == f) {
                continue;
            }
            final int pu = this.dp[u];
            final int pv = this.dp[v];
            final int su = this.sz[u];
            final int sv = this.sz[v];
            this.dp[u] -= this.dp[v] + this.sz[v];
            this.sz[u] -= this.sz[v];
            this.dp[v] += this.dp[u] + this.sz[u];
            this.sz[v] += this.sz[u];
            this.dfs2(v, u);
            this.dp[u] = pu;
            this.dp[v] = pv;
            this.sz[u] = su;
            this.sz[v] = sv;
        }
    }
}
