(ns pe-rest-testutils.core
  "A set of helper functions to aid in the unit testing of REST services."
  (:require [ring.mock.request :as mock]
            [pe-rest-utils.meta :as rumeta]))

(defn last-url-part
  "Returns the last path component of url-str. E.g., given
  http://www.example.com/abc/123, returns '123'"
  [url-str]
  (.substring url-str (inc (.lastIndexOf url-str "/"))))

(defn header
  "If val is not empty, a new request is returned containing a new header with
  name hdr and val for the value.  Otherwise req is returned."
  [req hdr val]
  (if (not (empty? val))
    (mock/header req hdr val)
    req))

(defn authorization-req-hdr-val
  "Returns the string value to use for an 'Authorization' request header based
  on the given parameters."
  [auth-scheme auth-scheme-param-name auth-token]
  (str auth-scheme " " auth-scheme-param-name "=\"" auth-token "\""))

(defn mt
  "Returns a media type string based on the given parameters."
  [mt-type
   mt-subtype
   version
   format-ind]
  (let [accept (format "%s/%s" mt-type mt-subtype)
        version (if version (format "-v%s" version) "")
        format-ind (if format-ind (format "+%s" format-ind) "")]
    (format "%s%s%s" accept version format-ind)))

(defn req-w-std-hdrs
  "Returns a mock request using the given parameters:

  mt-type - Type part of 'Accept' request header media type.
  mt-subtype - Subtype part of 'Accept' request header media type.
  version - Version part of 'Accept' request header media type.
  charset - Character set part of 'Accept' request header media type.
  format-ind - Format indicator part of 'Accept' request header media type.
  lang - 'Accept-Language' request header value.
  method - The HTTP method.
  uri - The request URI.
  hdr-apptxn-id - Application transaction ID header name.
  hdr-useragent-device-make - User-agent device make header name.
  hdr-useragent-device-os - User-agent device operating system header name.
  hdr-useragent-device-os-version - User-agent device operating system version
  header name.
  apptxn-id (optional) - The application transaction identifier to include as a
  request header.  If nil, a hard-coded value will be used."
  ([mt-type
    mt-subtype
    version
    charset
    format-ind
    lang
    method
    uri
    hdr-apptxn-id
    hdr-useragent-device-make
    hdr-useragent-device-os
    hdr-useragent-device-os-version]
   (req-w-std-hdrs mt-type
                   mt-subtype
                   version
                   charset
                   format-ind
                   lang
                   method
                   uri
                   hdr-apptxn-id
                   hdr-useragent-device-make
                   hdr-useragent-device-os
                   hdr-useragent-device-os-version
                   "TXN92019348"))
  ([mt-type
    mt-subtype
    version
    charset
    format-ind
    lang
    method
    uri
    hdr-apptxn-id
    hdr-useragent-device-make
    hdr-useragent-device-os
    hdr-useragent-device-os-version
    apptxn-id]
   (-> (mock/request method uri)
       (header "Accept" (mt mt-type mt-subtype version format-ind))
       (header "Accept-Charset" charset)
       (header "Accept-Language" lang)
       (header hdr-apptxn-id apptxn-id)
       (header hdr-useragent-device-make "iPhone")
       (header hdr-useragent-device-os "iOS")
       (header hdr-useragent-device-os-version "8.1.2"))))
