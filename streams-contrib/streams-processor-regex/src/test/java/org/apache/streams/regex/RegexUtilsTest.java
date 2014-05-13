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


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(Parameterized.class)
public class RegexUtilsTest {

    private final String pattern;
    private final String content;
    private final int wordMatchCount;
    private final int regularMatchCount;

    public RegexUtilsTest(String pattern, String content, int regularMatchCount, int wordMatchCount) {
        this.pattern = pattern;
        this.content = content;
        this.wordMatchCount = wordMatchCount;
        this.regularMatchCount = regularMatchCount;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][]{
                {"[0-9]*", "The number for emergencies is 911.", 1, 1},
                {"#\\w+", "This is#freakingcrazydude.", 1, 0},
                {"#\\w+", "This is #freakingcrazydude.", 1, 1},
                {"#\\w+", "This is #freakingcrazydude!", 1, 1},
                {"#\\w+", "This is #freakingcrazydude I went to the moon", 1, 1},
                {"#\\w+", "This is #freakingcrazydude I went to the #freakingcrazydude party", 2, 2},
                {"#\\w+", "This is#freakingcrazydude I went to the #freakingcrazydude party", 2, 1},
                {"#\\w+", "#what does the fox say?", 1, 1},
                {"#\\w+", "#what does the fox #say", 2, 2}
        });
    }


    @Test
    public void testMatches_simple() {
        List<String> wordResults = RegexUtils.extractWordMatches(this.pattern, this.content);
        assertThat(wordResults.size(), is(equalTo(wordMatchCount)));

        List<String> regularResults = RegexUtils.extractMatches(this.pattern, this.content);
        assertThat(regularResults.size(), is(equalTo(regularMatchCount)));
    }

}