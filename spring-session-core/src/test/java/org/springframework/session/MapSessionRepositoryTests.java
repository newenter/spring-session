/*
 * Copyright 2014-2017 the original author or authors.
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

package org.springframework.session;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MapSessionRepositoryTests {
	MapSessionRepository repository;

	MapSession session;

	@Before
	public void setup() {
		this.repository = new MapSessionRepository();
		this.session = new MapSession();
	}

	@Test
	public void getSessionExpired() {
		this.session.setMaxInactiveInterval(Duration.ofSeconds(1));
		this.session.setLastAccessedTime(Instant.now().minus(5, ChronoUnit.MINUTES));
		this.repository.save(this.session);

		assertThat(this.repository.getSession(this.session.getId())).isNull();
	}

	@Test
	public void createSessionDefaultExpiration() {
		Session session = this.repository.createSession();

		assertThat(session).isInstanceOf(MapSession.class);
		assertThat(session.getMaxInactiveInterval())
				.isEqualTo(new MapSession().getMaxInactiveInterval());
	}

	@Test
	public void createSessionCustomDefaultExpiration() {
		final Duration expectedMaxInterval = new MapSession().getMaxInactiveInterval()
				.plusSeconds(10);
		this.repository.setDefaultMaxInactiveInterval(
				(int) expectedMaxInterval.getSeconds());

		Session session = this.repository.createSession();

		assertThat(session.getMaxInactiveInterval())
				.isEqualTo(expectedMaxInterval);
	}
}
