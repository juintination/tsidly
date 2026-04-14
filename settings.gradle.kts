rootProject.name = "url-shortener"

include("common")
include("common:jpa")
include("common:serialization")
include("common:event")
include("common:outbox")
include("service")
include("service:shortener")
include("service:redirect")
include("service:gateway")
