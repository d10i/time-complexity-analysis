package tech.dario.timecomplexityanalysis.timerecorder.api;

public enum StaticTimeRecorderFactoryStatus {
  UNINITIALIZED,
  ONGOING_INITIALIZATION,
  FAILED_INITIALIZATION,
  SUCCESSFUL_INITIALIZATION,
  IMPL_NOT_FOUND_INITIALIZATION
}
