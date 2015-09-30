/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2014 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.sonar.server.computation.source;

import javax.annotation.CheckForNull;
import org.sonar.db.protobuf.DbFileSources;
import org.sonar.server.computation.scm.Changeset;
import org.sonar.server.computation.scm.ScmInfo;

public class ScmLineReader implements LineReader {

  private final ScmInfo scmReport;
  @CheckForNull
  private Changeset latestChange;

  public ScmLineReader(ScmInfo scmReport) {
    this.scmReport = scmReport;
  }

  @Override
  public void read(DbFileSources.Line.Builder lineBuilder) {
    Changeset changeset = scmReport.getChangesetForLine(lineBuilder.getLine());
    String author = changeset.getAuthor();
    if (author != null) {
      lineBuilder.setScmAuthor(author);
    }
    lineBuilder.setScmRevision(changeset.getRevision());
    lineBuilder.setScmDate(changeset.getDate());
    updateLatestChange(changeset);
  }

  private void updateLatestChange(Changeset newChangeSet) {
    if (latestChange == null) {
      latestChange = newChangeSet;
    } else {
      long newChangesetDate = newChangeSet.getDate();
      long latestChangeDate = latestChange.getDate();
      if (newChangesetDate > latestChangeDate) {
        latestChange = newChangeSet;
      }
    }
  }

  @CheckForNull
  public Changeset getLatestChange() {
    return latestChange;
  }
}
