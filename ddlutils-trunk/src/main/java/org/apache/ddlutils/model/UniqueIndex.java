package org.apache.ddlutils.model;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.ddlutils.util.StringUtils;

import java.util.ArrayList;

/**
 * Provides compatibility with Torque-style xml with separate &lt;index&gt; and
 * &lt;unique&gt; tags, but adds no functionality.  All indexes are treated the
 * same by the Table.
 * @version $Revision$
 */
public class UniqueIndex extends GenericIndex {
    /**
     * Unique ID for serialization purposes.
     */
    private static final long serialVersionUID = -4097003126550294993L;

    /**
     * {@inheritDoc}
     */
    public boolean isUnique() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Index copy() throws ModelException {
        UniqueIndex result = new UniqueIndex();
        result.name = name;
        result.columns = (ArrayList<IndexColumn>) columns.clone();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj) {
        if (obj instanceof UniqueIndex) {
            UniqueIndex other = (UniqueIndex) obj;

            return new EqualsBuilder().append(name, other.name)
                    .append(columns, other.columns)
                    .isEquals();
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equalsIgnoreCase(Index other) {
        if (other instanceof UniqueIndex) {
            UniqueIndex otherIndex = (UniqueIndex) other;
            boolean checkName = StringUtils.hasText(name, otherIndex.name);
            if ((!checkName || name.equalsIgnoreCase(otherIndex.name)) &&
                    (getColumnCount() == otherIndex.getColumnCount())) {
                for (int idx = 0; idx < getColumnCount(); idx++) {
                    if (!getColumn(idx).equalsIgnoreCase(otherIndex.getColumn(idx))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return columns.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Unique index [name=" +
                getName() +
                "; " +
                getColumnCount() +
                " columns]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toVerboseString() {
        StringBuilder result = new StringBuilder();
        result.append("Unique index [");
        result.append(getName());
        result.append("] columns:");
        for (int idx = 0; idx < getColumnCount(); idx++) {
            result.append(" ");
            result.append(getColumn(idx).toString());
        }
        return result.toString();
    }
}
