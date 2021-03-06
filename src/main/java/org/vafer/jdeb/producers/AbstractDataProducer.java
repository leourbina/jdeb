/*
 * Copyright 2013 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vafer.jdeb.producers;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.tools.ant.types.selectors.SelectorUtils;
import org.vafer.jdeb.DataProducer;
import org.vafer.jdeb.mapping.Mapper;

/**
 * Base Producer class providing including/excluding.
 *
 * @author Torsten Curdt
 */
public abstract class AbstractDataProducer implements DataProducer {

    private final String[] includes;
    private final String[] excludes;
    private final Mapper[] mappers;


    public AbstractDataProducer( final String[] pIncludes, final String[] pExcludes, final Mapper[] pMapper ) {
        excludes = (pExcludes != null) ? pExcludes : new String[0];
        includes = (pIncludes != null) ? pIncludes : new String[] { "**" };
        mappers = (pMapper != null) ? pMapper : new Mapper[0];
    }

    public boolean isIncluded( final String pName ) {
        if (!isIncluded(pName, includes)) {
            return false;
        }
        if (isExcluded(pName, excludes)) {
            return false;
        }
        return true;
    }

    private boolean isIncluded( String name, String[] includes ) {
        for (String include : includes) {
            if (SelectorUtils.matchPath(include, name)) {
                return true;
            }
        }
        return false;
    }


    private boolean isExcluded( String name, String[] excludes ) {
        for (String exclude : excludes) {
            if (SelectorUtils.matchPath(exclude, name)) {
                return true;
            }
        }
        return false;
    }

    public TarArchiveEntry map( final TarArchiveEntry pEntry ) {

        TarArchiveEntry entry = pEntry;

        for (Mapper mapper : mappers) {
            entry = mapper.map(entry);
        }

        return entry;
    }
}
