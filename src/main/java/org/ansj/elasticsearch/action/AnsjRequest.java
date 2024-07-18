package org.ansj.elasticsearch.action;

import org.elasticsearch.ElasticsearchGenerationException;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.support.single.shard.SingleShardRequest;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentFactory;
import org.elasticsearch.xcontent.XContentType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangqinghua on 16/2/2.
 */
public class AnsjRequest extends SingleShardRequest<AnsjRequest> {

    private String path;

    private Map<String, Object> args = new HashMap<>();

    private BytesReference source;

    public AnsjRequest() {
    }

    public AnsjRequest(String path) {
        this.path = path;
    }

    public AnsjRequest(StreamInput in) throws IOException {
        super(in);
        path = in.readString();
        args = in.readGenericMap();
        source = in.readBytesReference();
    }

    public String getPath() {
        return path;
    }

    public String get(String key) {
        return (String) args.get(key);
    }

    public String put(String key, String value) {
        return (String) args.put(key, value);
    }

    @Override
    public ActionRequestValidationException validate() {
        return null;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeString(path);
        out.writeGenericMap(args);
        out.writeBytesReference(source);
    }

    public Map<String, Object> asMap() {
        return args;
    }

    public BytesReference source() {
        return source;
    }

    public AnsjRequest source(Map<String, ?> querySource) {
        try {
            XContentBuilder builder = XContentFactory.contentBuilder(XContentType.JSON);
            builder.map(querySource);
            return source(builder);
        } catch (IOException e) {
            throw new ElasticsearchGenerationException("Failed to generate [" + querySource + "]", e);
        }
    }

    public AnsjRequest source(XContentBuilder builder) {
        this.source = BytesReference.bytes(builder);
        return this;
    }

    public AnsjRequest source(String querySource) {
        this.source = new BytesArray(querySource);
        return this;
    }

    public AnsjRequest source(byte[] querySource) {
        return source(querySource, 0, querySource.length);
    }

    public AnsjRequest source(byte[] querySource, int offset, int length) {
        return source(new BytesArray(querySource, offset, length));
    }

    public AnsjRequest source(BytesReference querySource) {
        this.source = querySource;
        return this;
    }
}
