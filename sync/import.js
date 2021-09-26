function import_filter(doc){
    console.log("********Processing import filter - documents from couchbase server");
      if (doc.type == 'project' || doc.type == 'location'){
        return true;
      }
      return false;
  }