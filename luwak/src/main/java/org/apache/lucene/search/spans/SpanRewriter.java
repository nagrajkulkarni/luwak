package org.apache.lucene.search.spans;
/*
 *   Copyright (c) 2015 Lemur Consulting Ltd.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import org.apache.lucene.search.*;

public class SpanRewriter {

    public static Query rewrite(Query in) {
        if (in instanceof SpanQuery)
            return in;
        if (in instanceof TermQuery)
            return rewriteTermQuery((TermQuery)in);
        if (in instanceof BooleanQuery)
            return rewriteBoolean((BooleanQuery) in);
        if (in instanceof MultiTermQuery)
            return rewriteMultiTermQuery((MultiTermQuery)in);

        throw new IllegalArgumentException("Don't know how to rewrite " + in.getClass());
    }

    public static Query rewriteTermQuery(TermQuery tq) {
        return new SpanTermQuery(tq.getTerm());
    }

    public static Query rewriteBoolean(BooleanQuery bq) {
        BooleanQuery newbq = new BooleanQuery();
        for (BooleanClause clause : bq) {
            newbq.add(rewrite(clause.getQuery()), clause.getOccur());
        }
        return newbq;
    }

    public static Query rewriteMultiTermQuery(MultiTermQuery mtq) {
        return new SpanMultiTermQueryWrapper<>(mtq);
    }

}
