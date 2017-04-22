package tech.dario.timecomplexityanalysis.agent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class Config {

  private final static Logger LOGGER = LoggerFactory.getLogger(Config.class);

  private final Set<String> whitelist;
  private final Set<String> blacklist;
  private final Boolean excludeStandardJavaApi;
  private final Boolean excludeStandardScalaApi;

  @JsonCreator
  public Config(
          @JsonProperty("whitelist") final Set<String> whitelist,
          @JsonProperty("blacklist") final Set<String> blacklist,
          @JsonProperty("excludeStandardJavaApi") final Boolean excludeStandardJavaApi,
          @JsonProperty("excludeStandardScalaApi") final Boolean excludeStandardScalaApi) {
    this.whitelist = whitelist;
    this.blacklist = blacklist;
    this.excludeStandardJavaApi = excludeStandardJavaApi;
    this.excludeStandardScalaApi = excludeStandardScalaApi;
  }

  public static Config getDefault() {
    return new Config(new HashSet<>(), new HashSet<>(), true, true);
  }

  public static Config fromJsonFilePath(final String filePath) throws IOException {
    // Look up in class path
    final URL url = MeasuringAgent.class.getClassLoader().getResource(filePath);
    if (url != null) {
      LOGGER.debug("File '{}' found in classpath", filePath);
      final File file = getFileFromUrl(url);
      return fromJsonFile(file);
    }

    // Look up in file system
    final File file = new File(filePath);
    return fromJsonFile(file);
  }

  public static Config fromJsonFile(final File file) throws IOException {
    if (!file.exists()) {
      LOGGER.error("Config JSON file does not exist '{}' ", file);
      throw new FileNotFoundException("Config JSON file does not exist: " + file);
    }

    final ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(file, Config.class);
  }

  private static File getFileFromUrl(final URL url) {
    try {
      return new File(url.toURI());
    } catch (URISyntaxException e) {
      return new File(url.getPath());
    }
  }

  public Set<String> getWhitelist() {
    return whitelist;
  }

  public Set<String> getBlacklist() {
    return blacklist;
  }

  public boolean isExcludeStandardJavaApi() {
    if (excludeStandardJavaApi == null) {
      // Exclude by default
      return true;
    }

    return excludeStandardJavaApi;
  }

  public boolean isExcludeStandardScalaApi() {
    if (excludeStandardScalaApi == null) {
      // Exclude by default
      return true;
    }

    return excludeStandardScalaApi;
  }
}
