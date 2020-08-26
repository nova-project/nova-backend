package net.getnova.backend.sql;

import org.hibernate.cfg.Environment;

import java.nio.charset.Charset;
import java.util.Properties;

class SqlProperties extends Properties {

  SqlProperties(final Charset charset) {
    super();
    this.put(Environment.AUTOCOMMIT, "false");
    this.put(Environment.HBM2DDL_AUTO, "update");
    this.put(Environment.HBM2DDL_CHARSET_NAME, charset.toString());
    this.put(Environment.CONNECTION_PREFIX + ".CharSet", charset.toString());
    this.put(Environment.CONNECTION_PREFIX + ".characterEncoding", charset.toString());
    this.put(Environment.CONNECTION_PREFIX + ".useUnicode", "true");
  }
}
