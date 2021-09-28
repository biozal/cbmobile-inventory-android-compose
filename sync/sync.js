function sync(doc, oldDoc) {

  /* Data Validation */
  validateNotEmpty("type", doc.type);

  if (doc.type == 'location') {
    console.log("********Processing Location Docs - setting it to global/public");
    channel('!');
  } else if (doc.type == 'project') {
    console.log("********Processing Team Docs");
    validateNotEmpty("team", doc.team);
    /* Routing  -- add channel routing rules here for document */
    var team = getTeam();
    var channelId = "channel." + team;
    console.log("********Setting Channel to " + channelId);
    channel(channelId);

    /* Authorization  - Access Control */
    requireRole(team);

    console.log("********granting access to channels");
    access("role:team1", "channel.team1")
    access("role:team2", "channel.team2")
    access("role:team3", "channel.team3")
  } else {
    if (!isDelete()) {
    }
  }

  // get type property
  function getType() {
    return (isDelete() ? oldDoc.type : doc.type);
  }

  // get email Id property
  function getTeam() {
    return (isDelete() ? oldDoc.team : doc.team);
  }

  // Check if document is being created/added for first time
  function isCreate() {
    // Checking false for the Admin UI to work
    return ((oldDoc == false) || (oldDoc == null || oldDoc._deleted) && !isDelete());
  }

  // Check if this is a document delete
  function isDelete() {
    return (doc._deleted == true);
  }

  // Verify that specified property exists
  function validateNotEmpty(key, value) {
    if (!value) {
      throw ({ forbidden: key + " is not provided." });
    }
  }
}