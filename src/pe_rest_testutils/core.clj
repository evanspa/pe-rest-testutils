(ns pe-rest-testutils.core
  (:require [ring.mock.request :as mock]
            [pe-rest-utils.meta :as rumeta]))

(defn last-url-part
  [url-str]
  (.substring url-str (inc (.lastIndexOf url-str "/"))))

(defn header [req hdr val]
  (if (not (empty? val))
    (mock/header req hdr val)
    req))

(defn authorization-req-hdr-val
  [auth-scheme auth-scheme-param-name auth-token]
  (str auth-scheme " " auth-scheme-param-name "=\"" auth-token "\""))

(defn mt
  [mt-type
   mt-subtype
   version
   format-ind]
  (let [accept (format "%s/%s" mt-type mt-subtype)
        version (if version (format "-v%s" version) "")
        format-ind (if format-ind (format "+%s" format-ind) "")]
    (format "%s%s%s" accept version format-ind)))

(defn req-w-std-hdrs
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
