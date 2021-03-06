/*
 * SonarQube
 * Copyright (C) 2009-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.batch.report;

import java.io.File;
import java.util.Date;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.CoreProperties;
import org.sonar.api.batch.bootstrap.ProjectDefinition;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.batch.index.BatchComponentCache;
import org.sonar.batch.protocol.output.BatchReport;
import org.sonar.batch.protocol.output.BatchReportReader;
import org.sonar.batch.protocol.output.BatchReportWriter;
import org.sonar.batch.scan.ImmutableProjectReactor;

import static org.assertj.core.api.Assertions.assertThat;

public class MetadataPublisherTest {

  @Rule
  public TemporaryFolder temp = new TemporaryFolder();

  private ProjectDefinition projectDef;
  private Project project;
  private MetadataPublisher underTest;
  private Settings settings;

  @Before
  public void prepare() {
    projectDef = ProjectDefinition.create().setKey("foo");
    project = new Project("foo").setAnalysisDate(new Date(1234567L));
    BatchComponentCache componentCache = new BatchComponentCache();
    org.sonar.api.resources.Resource sampleFile = org.sonar.api.resources.File.create("src/Foo.php").setEffectiveKey("foo:src/Foo.php");
    componentCache.add(project, null);
    componentCache.add(sampleFile, project);
    settings = new Settings();
    underTest = new MetadataPublisher(componentCache, new ImmutableProjectReactor(projectDef), settings);
  }

  @Test
  public void write_metadata() throws Exception {
    settings.setProperty(CoreProperties.CPD_CROSS_PROJECT, "true");
    File outputDir = temp.newFolder();
    BatchReportWriter writer = new BatchReportWriter(outputDir);

    underTest.publish(writer);

    BatchReportReader reader = new BatchReportReader(outputDir);
    BatchReport.Metadata metadata = reader.readMetadata();
    assertThat(metadata.getAnalysisDate()).isEqualTo(1234567L);
    assertThat(metadata.getProjectKey()).isEqualTo("foo");
    assertThat(metadata.getProjectKey()).isEqualTo("foo");
    assertThat(metadata.getCrossProjectDuplicationActivated()).isTrue();
  }

  @Test
  public void write_project_branch() throws Exception {
    settings.setProperty(CoreProperties.CPD_CROSS_PROJECT, "true");
    settings.setProperty(CoreProperties.PROJECT_BRANCH_PROPERTY, "myBranch");
    projectDef.properties().put(CoreProperties.PROJECT_BRANCH_PROPERTY, "myBranch");
    project.setKey("foo:myBranch");
    project.setEffectiveKey("foo:myBranch");

    File outputDir = temp.newFolder();
    BatchReportWriter writer = new BatchReportWriter(outputDir);

    underTest.publish(writer);

    BatchReportReader reader = new BatchReportReader(outputDir);
    BatchReport.Metadata metadata = reader.readMetadata();
    assertThat(metadata.getAnalysisDate()).isEqualTo(1234567L);
    assertThat(metadata.getProjectKey()).isEqualTo("foo");
    assertThat(metadata.getBranch()).isEqualTo("myBranch");
    // Cross project duplication disabled on branches
    assertThat(metadata.getCrossProjectDuplicationActivated()).isFalse();
  }

}
