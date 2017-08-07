package com.venkat.section5.cluster

import com.venkat.section5.commons.Add

object ClusterApp extends App{
  //initiate frontend node
  Frontend.initiate()

  //initiate three nodes from backend
  Baackend.initiate(2552)
  Baackend.initiate(2560)
  Baackend.initiate(2561)

  Thread.sleep(10000)
  Frontend.getFrontend ! Add(2,4)

}
