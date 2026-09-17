record Edge(Vertex fromVertex, Vertex toVertex, Integer distance) {

@Override
    public boolean equals(Object other) {
    if (this == other) return true;
    if (other instanceof Edge edge)
        return this.fromVertex.equals(edge.fromVertex) && this.toVertex.equals(edge.toVertex);
    return false;
}

    @Override
    public String toString() {
        return "Edge{fromVertex=%s, toVertex=%s, distance=%s}"
            .formatted(fromVertex, toVertex, distance);
    }
}
