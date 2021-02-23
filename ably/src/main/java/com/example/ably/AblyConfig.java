package com.example.ably;

import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.rest.AblyRest;
import io.ably.lib.types.AblyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AblyConfig
{
	@Value( "${ABLY_API_KEY}" )
	private String apiKey;

	@Bean
	protected AblyRealtime ablyRealtime() {
		try {
			return new AblyRealtime(apiKey);
		} catch (AblyException exception) {
			return null;
		}
	}

	@Bean
	public AblyRest ablyRest() {
		try {
			return new AblyRest(apiKey);
		} catch (AblyException exception) {
			return null;
		}
	}
}
