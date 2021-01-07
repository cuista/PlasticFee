package it.unical.computerscience.pfsociety.plasticfee.data;

import it.unical.computerscience.pfsociety.plasticfee.protobuf.ExampleProto;

import java.util.Map;

public class CommunityExampleRepository {
    Map<Integer, ExampleProto.Community> communities;

    public CommunityExampleRepository(Map<Integer, ExampleProto.Community> communities) {
        this.communities = communities;
    }

    public ExampleProto.Community getCommunity(int id) {
        return communities.get(id);
    }
}
