public <T> List<DnsConfiguration> buildHubSpotDnsRecordUpdatesForZones(T entity) {
  List<DnsConfiguration> hubSpotDnsChanges = new ArrayList<>();
  Set<DnsRecordBuilder<T>> buildersToApply = getDnsRecordBuildersToApplyOrThrow(entity);

  for (DnsRecordBuilder<T> dnsRecordBuilder : buildersToApply) {
    Map<String, DnsConfiguration> requiredConfigByZone = dnsRecordBuilder.getDnsConfigurationByZone(entity);
    for (Entry<String, DnsConfiguration> entry : requiredConfigByZone.entrySet()) {
      String zone = entry.getKey();
      DnsConfiguration config = entry.getValue();
      List<RecordRequest> changedHubSpotDomainRecords = dnsClientHelper.findRecordsToUpdate(config.getRecords(), zone);
      hubSpotDnsChanges.add(config.withRecords(changedHubSpotDomainRecords));
    }
  }

  return hubSpotDnsChanges;
}

private <T> Set<DnsRecordBuilder<T>> getDnsRecordBuildersToApplyOrThrow(T entity) {
  if (entity instanceof Account) {
    return accountRecordBuilders.stream()
        .map(accountDnsRecordBuilder -> (DnsRecordBuilder<T>) accountDnsRecordBuilder)
        .collect(Collectors.toSet());
  } else if (entity instanceof IpAddress) {
    return ipRecordBuilders.stream()
        .map(ipAddressDnsRecordBuilder -> (DnsRecordBuilder<T>) ipAddressDnsRecordBuilder)
        .collect(Collectors.toSet());
  } else if (entity instanceof Integer) {
    return portalDnsRecordBuilders.stream()
        .map(portalDnsRecordBuilder -> (DnsRecordBuilder<T>) portalDnsRecordBuilder)
        .collect(Collectors.toSet());
  } else {
    throw new IllegalArgumentException(String.format("Invalid type given: %s", entity.getClass()));
  }
}