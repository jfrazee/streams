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

package org.apache.streams.regex;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides utilities for extracting matches from content
 */
public class RegexUtils {

    private static final Map<String, Pattern> patternCache = new ConcurrentHashMap<String, Pattern>();

    private RegexUtils() {}

    /**
     * Extracts matches of the given pattern in the content and returns them as a list.
     * @param pattern the pattern for the substring to match.  For example, [0-9]* matches 911 in Emergency number is 911.
     * @param content the complete content to find matches in.
     * @return a non-null list of matches.
     */
    public static List<String> extractMatches(String pattern, String content) {
        return getMatches(pattern, content, -1);
    }

    /**
     * Extracts matches of the given pattern that are bounded by separation characters and returns them as a list.
     * @param pattern the pattern for the substring to match.  For example, [0-9]* matches 911 in Emergency number is 911.
     * @param content the complete content to find matches in.
     * @return a non-null list of matches.
     */
    public static List<String> extractWordMatches(String pattern, String content) {
        pattern = "(^|\\s)(" + pattern + ")([\\s!\\.;,]|$)";
        return getMatches(pattern, content, 2);
    }

    protected static List<String> getMatches(String pattern, String content, int capture) {
        Matcher m = getPattern(pattern).matcher(content);
        List<String> result = new LinkedList<String>();
        while(m.find()) {
            String group = capture > 0 ? m.group(capture) : m.group();
            if(group != null && !group.equals("")) {
                result.add(group);
            }
        }
        return result;
    }

    private static Pattern getPattern(String pattern) {
        Pattern p;
        if (patternCache.containsKey(pattern)) {
            p = patternCache.get(pattern);
        } else {
            p = Pattern.compile(pattern);
            patternCache.put(pattern, p);
        }
        return p;
    }


}
